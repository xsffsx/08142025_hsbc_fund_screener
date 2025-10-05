#!/usr/bin/env python3
"""
Generate Markdown tables for U43051 from 8 JSON response bodies with list de-duplication.
- Output Markdown files into ./doc/ with naming: yyyymmddhhmmss_XX_Analysis_*
- Split advanceChart daily NAV by year into separate files.
- FundSearchCriteria: write minMaxCriterias into the main datatables file; listCriterias keep only items related to BlackRock (MERRL) and deduplicate into a separate file.
"""
import json
from pathlib import Path
from datetime import datetime
from collections import defaultdict

ROOT = Path(__file__).parent
OUTPUT_DIR = ROOT / "output"
DOC_DIR = ROOT / "doc"

# Source JSON files
FILES = {
    "product": OUTPUT_DIR / "001_U43051.done_amh_ut_product_response_body.json",
    "advance": OUTPUT_DIR / "001_U43051.done_wmds_advanceChart_response_body.json",
    "summary": OUTPUT_DIR / "001_U43051.done_wmds_fundQuoteSummary_response_body.json",
    "criteria": OUTPUT_DIR / "001_U43051.done_wmds_fundSearchCriteria_response_body.json",
    "holding": OUTPUT_DIR / "001_U43051.done_wmds_holdingAllocation_response_body.json",
    "classes": OUTPUT_DIR / "001_U43051.done_wmds_otherFundClasses_response_body.json",
    "quote": OUTPUT_DIR / "001_U43051.done_wmds_quoteDetail_response_body.json",
    "top10": OUTPUT_DIR / "001_U43051.done_wmds_topTenHoldings_response_body.json",
}

TS = datetime.now().strftime("%Y%m%d%H%M%S")

MAIN_FILE = DOC_DIR / f"{TS}_02_Analysis_Fund_U43051_DataTables.md"
ADV_TPL = f"{TS}_03_Analysis_Fund_U43051_AdvanceChart_{{year}}.md"
LIST_FILE = DOC_DIR / f"{TS}_07_Analysis_Fund_U43051_FundSearchCriteria_ListCriterias_MERRL.md"


def md_table(headers, rows):
    lines = ["| " + " | ".join(headers) + " |",
             "| " + " | ".join(["---"] * len(headers)) + " |"]
    for r in rows:
        lines.append("| " + " | ".join(str(x) if x is not None else "" for x in r) + " |")
    return "\n".join(lines) + "\n\n"


def write(path: Path, content: str):
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(content, encoding="utf-8")


def load_json(path: Path):
    with path.open("r", encoding="utf-8") as f:
        return json.load(f)


def gen_product_section():
    data = load_json(FILES["product"])['payload'][0]['attributeMap']
    rows = [(k, data.get(k)) for k in sorted(data.keys())]
    return "### 产品基本信息 (amh_ut_product)\n\n" + md_table(["Key", "Value"], rows)


def gen_summary_section():
    s = load_json(FILES["summary"])['summary']
    # core summary
    core_rows = [
        ("bid", s.get("bid")),
        ("offer", s.get("offer")),
        ("weekRangeLow", s.get("weekRangeLow")),
        ("weekRangeHigh", s.get("weekRangeHigh")),
        ("weekRangeCurrency", s.get("weekRangeCurrency")),
    ]
    out = "### 基金报价摘要 (wmds_fundQuoteSummary)\n\n"
    out += md_table(["Field", "Value"], core_rows)
    # calendar year returns
    cy = s.get('calendarYearTotalReturns', {}).get('items', [])
    cy_rows = []
    for it in cy:
        idx = it.get('prospectusPrimaryIndexYearReturns', [])
        idx_name = idx[0]['indexName'] if idx else None
        cy_rows.append((it.get('year'), it.get('fundCalendarYearReturn'), it.get('categoryCalendarYearReturn'), idx_name))
    out += md_table(["Year", "Fund CY Return", "Category CY Return", "Index"], cy_rows)
    return out


def gen_quote_section():
    q = load_json(FILES["quote"])['priceQuote']
    # overview fields
    overview_keys = [
        'symbol','market','productType','productSubTypeCode','companyName','currency','priceQuote','changeAmount','changePercent',
        'bidPrice','bidPriceDate','allowBuy','allowSell','riskLvlCde','performanceId','tradableCurrency','launchDate'
    ]
    rows = [(k, q.get(k)) for k in overview_keys]
    out = "### 详细报价信息 (wmds_quoteDetail)\n\n" + md_table(["Field","Value"], rows)
    # calendar returns
    cr_items = (q.get('calendarReturns') or {}).get('items', [])
    cr_rows = [(it.get('year'), it.get('annualCalendarYearReturn')) for it in cr_items]
    if cr_rows:
        out += md_table(["Year","Annual Return"], cr_rows)
    # cumulative returns
    cum_items = (q.get('cumulativeReturns') or {}).get('items', [])
    cum_rows = [(it.get('period'), it.get('trailingTotalReturn'), it.get('dailyPerformanceNAV')) for it in cum_items]
    if cum_rows:
        out += md_table(["Period","Trailing Total Return","Daily Performance NAV"], cum_rows)
    return out


def gen_holding_section():
    h = load_json(FILES["holding"]) 
    tables = []
    for item in h['holdingAllocation']:
        method = item.get('methods')
        bds = item.get('breakdowns', [])
        # de-dup by (name, weighting)
        seen = set()
        rows = []
        for bd in bds:
            key = (bd.get('name'), bd.get('weighting'))
            if key in seen:
                continue
            seen.add(key)
            rows.append((bd.get('name'), bd.get('weighting')))
        tables.append(f"#### {method}\n\n" + md_table(["Name","Weighting%"], rows))
    meta_rows = [("numberOfStockHoldings", h.get("numberOfStockHoldings")),
                 ("numberOfBondHoldings", h.get("numberOfBondHoldings")),
                 ("lastUpdatedDate", h.get("lastUpdatedDate"))]
    tables.append(md_table(["Field","Value"], meta_rows))
    return "### 持仓分配 (wmds_holdingAllocation)\n\n" + "\n".join(tables)


def gen_top10_section():
    t = load_json(FILES["top10"])['top10Holdings']
    items = t.get('items', [])
    rows = []
    seen = set()
    for it in items:
        key = (it.get('securityName'), it.get('market'))
        if key in seen:
            continue
        seen.add(key)
        rows.append((it.get('securityName'), it.get('market'), it.get('weighting'), it.get('marketValue'), it.get('currency')))
    out = "### 前十大持仓 (wmds_topTenHoldings)\n\n" + md_table(["Security","Market","Weighting%","Mkt Value","CCY"], rows)
    out += f"最后更新: {t.get('lastUpdatedDate')}\n\n"
    return out


def gen_classes_section():
    c = load_json(FILES["classes"]).get('assetClasses')
    if c is None:
        return "### 其他基金类别 (wmds_otherFundClasses)\n\n无可用数据 (assetClasses = null)\n\n"
    rows = []
    seen = set()
    for it in c:
        key = (it.get('code'), it.get('name'))
        if key in seen:
            continue
        seen.add(key)
        rows.append((it.get('code'), it.get('name')))
    return "### 其他基金类别 (wmds_otherFundClasses)\n\n" + md_table(["Code","Name"], rows)


def gen_criteria_minmax_section():
    c = load_json(FILES["criteria"]) 
    mm = c.get('minMaxCriterias', [])
    # de-dup by (criteriaKey, min, max)
    seen = set(); rows = []
    for it in mm:
        key = (it.get('criteriaKey'), it.get('minimum'), it.get('maximum'))
        if key in seen:
            continue
        seen.add(key)
        rows.append((it.get('criteriaKey'), it.get('minimum'), it.get('maximum')))
    return "### 搜索条件-数值范围 (wmds_fundSearchCriteria.minMaxCriterias)\n\n" + md_table(["CriteriaKey","Min","Max"], rows)


def gen_criteria_list_file():
    # Only items related to BlackRock (MERRL) per spec to keep file concise
    c = load_json(FILES["criteria"]) 
    list_crit = c.get('listCriterias', [])
    blocks = []
    for block in list_crit:
        key = block.get('criteriaKey')
        items = block.get('items', [])
        # Filter: keep items where itemKey == 'MERRL' or itemValue contains 'BlackRock'
        filtered = []
        seen = set()
        for it in items:
            ik = it.get('itemKey')
            iv_raw = it.get('itemValue')
            ik_str = str(ik) if ik is not None else ""
            iv = str(iv_raw) if iv_raw is not None else ""
            if not (ik_str == 'MERRL' or ('BlackRock' in iv)):
                continue
            tup = (ik_str, iv, it.get('parent'))
            if tup in seen:
                continue
            seen.add(tup)
            filtered.append((ik, iv, it.get('parent')))
        if filtered:
            blocks.append(f"#### {key}\n\n" + md_table(["ItemKey","ItemValue","Parent"], filtered))
    if not blocks:
        content = "# FundSearchCriteria ListCriterias (MERRL only)\n\n无与 BlackRock (MERRL) 相关的列表项。\n"
    else:
        content = "# FundSearchCriteria ListCriterias (MERRL only)\n\n" + "\n".join(blocks)
    write(LIST_FILE, content)


def gen_advance_by_year():
    adv = load_json(FILES["advance"])['result'][0]['data']
    # group by year
    by_year = defaultdict(list)
    seen_dates = set()
    for d in adv:
        dt = d.get('date'); nav = d.get('navPrice'); cr = d.get('cumulativeReturn')
        if dt in seen_dates:
            continue
        seen_dates.add(dt)
        year = dt.split('-')[0]
        by_year[year].append((dt, nav, cr))
    for year in sorted(by_year.keys()):
        rows = sorted(by_year[year], key=lambda x: x[0])
        content = f"# AdvanceChart Daily NAV — {year}\n\n" + md_table(["Date","NAV","CumulativeReturn"], rows)
        write(DOC_DIR / ADV_TPL.format(year=year), content)


def main():
    main_parts = [
        "# U43051 Data Tables\n\n",
        gen_product_section(),
        gen_summary_section(),
        gen_quote_section(),
        gen_holding_section(),
        gen_classes_section(),
        gen_criteria_minmax_section(),
        f"> 备注：FundSearchCriteria 的 ListCriterias（与 BlackRock/MERRL 相关部分）已单独输出至 {LIST_FILE.name}。\n\n",
    ]
    write(MAIN_FILE, "".join(main_parts))
    gen_advance_by_year()
    gen_criteria_list_file()
    print("Generated:")
    print(MAIN_FILE)
    # List advance files
    for p in sorted(DOC_DIR.glob(f"{TS}_03_Analysis_Fund_U43051_AdvanceChart_*.md")):
        print(p)
    print(LIST_FILE)

if __name__ == "__main__":
    main()

