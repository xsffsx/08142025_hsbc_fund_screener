
package com.hhhh.group.secwealth.mktdata.fund.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UTHoldingAlloc;
import com.hhhh.group.secwealth.mktdata.common.exception.CommonException;
import com.hhhh.group.secwealth.mktdata.common.system.constants.DateConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ErrTypeConstants;
import com.hhhh.group.secwealth.mktdata.common.util.DateUtil;
import com.hhhh.group.secwealth.mktdata.common.util.ListUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.fund.AbstractMdsbeService;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.HoldingAllocationRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.HoldingAllocationResponse;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.holdingallocation.Breakdowns;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.holdingallocation.HoldingAllocation;
import com.hhhh.group.secwealth.mktdata.fund.dao.HoldingAllocationDao;
import com.hhhh.group.secwealth.mktdata.fund.util.MiscUtil;


@Service("holdingAllocationService")
public class HoldingAllocationServiceImpl extends AbstractMdsbeService {

    @Autowired
    @Qualifier("holdingAllocationDao")
    private HoldingAllocationDao holdingAllocationDao;

    @Override
    public Object execute(final Object object) throws Exception {

        // Prepare Request
        HoldingAllocationRequest request = (HoldingAllocationRequest) object;

        // Set Response
        HoldingAllocationResponse response = new HoldingAllocationResponse();

        // get performanceId from Primary table
        List<Object[]> holdingAllocList = this.holdingAllocationDao.searchHoldingAllocList(request);

        String performanceId = null;
        SimpleDateFormat df = new SimpleDateFormat(DateConstants.DateFormat_yyyyMMdd_withHyphen);
        if (ListUtil.isValid(holdingAllocList)) {
            if (holdingAllocList.size() == 1) {
                Object[] holdingAlloc = holdingAllocList.get(0);
                performanceId = MiscUtil.safeString(holdingAlloc[0]);
                response.setNumberOfBondHoldings(MiscUtil.safeBigDecimalValue(holdingAlloc[1]));
                response.setNumberOfStockHoldings(MiscUtil.safeBigDecimalValue(holdingAlloc[2]));
                String lastUpdatedDate = MiscUtil.safeString(holdingAlloc[3]);
                if (StringUtil.isValid(lastUpdatedDate)) {
                    response.setLastUpdatedDate(
                        DateUtil.string2FormatDateString(df.parse(lastUpdatedDate), DateConstants.DateFormat_yyyyMMdd_withHyphen));
                }
            } else {
                LogUtil.error(HoldingAllocationServiceImpl.class,
                    "the HoldingAllocationList is invalid, size = : " + holdingAllocList.size());
                throw new CommonException(ErrTypeConstants.DB_DATA_UNAVAILABLE);
            }
        }

        // get UTHoldingAlloc from DB
        List<UTHoldingAlloc> holdingAllocations = this.holdingAllocationDao.searchHoldingAllocation(performanceId);

        // Set value from DB
        if (ListUtil.isValid(holdingAllocations)) {
            List<HoldingAllocation> holdingAllocationList = new ArrayList<HoldingAllocation>();
            HoldingAllocation assetAlloc = new HoldingAllocation();
            HoldingAllocation stockSectors = new HoldingAllocation();
            HoldingAllocation equityRregional = new HoldingAllocation();
            HoldingAllocation bondSectors = new HoldingAllocation();
            HoldingAllocation bondRegional = new HoldingAllocation();
            List<Breakdowns> assetAllocations = new ArrayList<Breakdowns>();
            List<Breakdowns> globalStockSectors = new ArrayList<Breakdowns>();
            List<Breakdowns> equityRregionalExposures = new ArrayList<Breakdowns>();
            List<Breakdowns> globalBondSectors = new ArrayList<Breakdowns>();
            List<Breakdowns> bondRegionalExposures = new ArrayList<Breakdowns>();

            for (UTHoldingAlloc holdingAlloc : holdingAllocations) {
                if (null != holdingAlloc) {
                    assetAlloc = getAssetAlloc(holdingAlloc, assetAlloc, assetAllocations);
                    stockSectors = getStockSectors(holdingAlloc, stockSectors, globalStockSectors);
                    equityRregional = getEquityRregional(holdingAlloc, equityRregional, equityRregionalExposures);
                    bondSectors = getBondSectors(holdingAlloc, bondSectors, globalBondSectors);
                    bondRegional = getBondRegional(holdingAlloc, bondRegional, bondRegionalExposures);
                }
            }
            holdingAllocationList.add(assetAlloc);
            holdingAllocationList.add(stockSectors);
            holdingAllocationList.add(equityRregional);
            holdingAllocationList.add(bondSectors);
            holdingAllocationList.add(bondRegional);
            response.setHoldingAllocation(holdingAllocationList);
        }
        return response;
    }

    private HoldingAllocation getAssetAlloc(final UTHoldingAlloc holdingAlloc, final HoldingAllocation assetAlloc,
        final List<Breakdowns> assetAllocations) throws ParseException {

        // set "assetAllocations" list
        if (holdingAlloc.getUtHoldingAllocId().getHoldingAllocClassType().equals("ASET_ALLOC")) {
            assetAlloc.setMethods("assetAllocations");
            Breakdowns breakdowns = new Breakdowns();
            breakdowns.setName(holdingAlloc.getUtHoldingAllocId().getHoldingAllocClassName());
            breakdowns.setWeighting(holdingAlloc.getHoldingAllocWeightNet());
            assetAlloc.setPortfolioDate(
                DateUtil.string2FormatDateString(holdingAlloc.getPortfolioDate(), DateConstants.DateFormat_yyyyMMdd_withHyphen));
            assetAllocations.add(breakdowns);
            assetAlloc.setBreakdowns(assetAllocations);
        }
        return assetAlloc;
    }

    private HoldingAllocation getStockSectors(final UTHoldingAlloc holdingAlloc, final HoldingAllocation stockSectors,
        final List<Breakdowns> globalStockSectors) throws ParseException {

        // set "globalStockSectors" list
        if (holdingAlloc.getUtHoldingAllocId().getHoldingAllocClassType().equals("STOCK_SEC")) {
            stockSectors.setMethods("globalStockSectors");
            Breakdowns breakdowns = new Breakdowns();
            breakdowns.setName(holdingAlloc.getUtHoldingAllocId().getHoldingAllocClassName());
            breakdowns.setWeighting(holdingAlloc.getHoldingAllocWeightNet());
            stockSectors.setPortfolioDate(
                DateUtil.string2FormatDateString(holdingAlloc.getPortfolioDate(), DateConstants.DateFormat_yyyyMMdd_withHyphen));

            globalStockSectors.add(breakdowns);
            stockSectors.setBreakdowns(globalStockSectors);
        }
        return stockSectors;
    }

    private HoldingAllocation getEquityRregional(final UTHoldingAlloc holdingAlloc, final HoldingAllocation equityRregional,
        final List<Breakdowns> equityRregionalExposures) throws ParseException {

        // get "equityRregionalExposures" list
        if (holdingAlloc.getUtHoldingAllocId().getHoldingAllocClassType().equals("STOCK_GEO")) {
            equityRregional.setMethods("regionalExposures");
            Breakdowns breakdowns = new Breakdowns();
            breakdowns.setName(holdingAlloc.getUtHoldingAllocId().getHoldingAllocClassName());
            breakdowns.setWeighting(holdingAlloc.getHoldingAllocWeightNet());
            equityRregional.setPortfolioDate(
                DateUtil.string2FormatDateString(holdingAlloc.getPortfolioDate(), DateConstants.DateFormat_yyyyMMdd_withHyphen));
            equityRregionalExposures.add(breakdowns);
            equityRregional.setBreakdowns(equityRregionalExposures);
        }
        return equityRregional;
    }

    private HoldingAllocation getBondSectors(final UTHoldingAlloc holdingAlloc, final HoldingAllocation bondSectors,
        final List<Breakdowns> globalBondSectors) throws ParseException {

        // get "globalBondSectors" list
        if (holdingAlloc.getUtHoldingAllocId().getHoldingAllocClassType().equals("BOND_SEC")) {
            bondSectors.setMethods("globalBondSectors");
            Breakdowns breakdowns = new Breakdowns();
            breakdowns.setName(holdingAlloc.getUtHoldingAllocId().getHoldingAllocClassName());
            breakdowns.setWeighting(holdingAlloc.getHoldingAllocWeightNet());
            bondSectors.setPortfolioDate(
                DateUtil.string2FormatDateString(holdingAlloc.getPortfolioDate(), DateConstants.DateFormat_yyyyMMdd_withHyphen));

            globalBondSectors.add(breakdowns);
            bondSectors.setBreakdowns(globalBondSectors);
        }
        return bondSectors;
    }

    private HoldingAllocation getBondRegional(final UTHoldingAlloc holdingAlloc, final HoldingAllocation bondRegional,
        final List<Breakdowns> bondRegionalExposures) throws ParseException {

        // set "bondRegionalExposures" list
        if (holdingAlloc.getUtHoldingAllocId().getHoldingAllocClassType().equals("BOND_GEO")) {
            bondRegional.setMethods("bondRegionalExposures");
            Breakdowns breakdowns = new Breakdowns();
            breakdowns.setName(holdingAlloc.getUtHoldingAllocId().getHoldingAllocClassName());
            breakdowns.setWeighting(holdingAlloc.getHoldingAllocWeightNet());
            bondRegional.setPortfolioDate(
                DateUtil.string2FormatDateString(holdingAlloc.getPortfolioDate(), DateConstants.DateFormat_yyyyMMdd_withHyphen));

            bondRegionalExposures.add(breakdowns);
            bondRegional.setBreakdowns(bondRegionalExposures);
        }
        return bondRegional;
    }
}
