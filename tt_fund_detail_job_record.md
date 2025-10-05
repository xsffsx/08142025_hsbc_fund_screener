
# 连接数据库
docker run --rm -e PGPASSWORD=hsbc_pass postgres:16 psql \
  -h host.docker.internal -p 5433 -U hsbc_user -d wdv -v ON_ERROR_STOP=1 \
  -c "SET lock_timeout='5s'; "

# tt_fund_detail_job_record
table:
wdv.public.tt_fund_detail_job_record
wdv.public.tt_fund_detail_job_record.id
wdv.public.tt_fund_detail_job_record.batch_name
wdv.public.tt_fund_detail_job_record.biz_date
wdv.public.tt_fund_detail_job_record.key1
wdv.public.tt_fund_detail_job_record.endpoint
wdv.public.tt_fund_detail_job_record.status
wdv.public.tt_fund_detail_job_record.attempts
wdv.public.tt_fund_detail_job_record.input_msg
wdv.public.tt_fund_detail_job_record.output_msg
wdv.public.tt_fund_detail_job_record.err_msg_json_arr
wdv.public.tt_fund_detail_job_record.crt_dt_tm
wdv.public.tt_fund_detail_job_record.updt_dt_tm

# /Users/paulo/PycharmProjects/20250809_MCP/08132025_hsbc_fund_screener/data_20250813_212612 
data_20250813_212612/001_U43051.done/amh_ut_product.response.json   
-> response_body -> json -> wdv.public.tt_fund_detail_job_record.output_msg
-> endpoint -> product
-> key1 -> <product_code>
-> batch_name=fund_detail

data_20250813_212612/001_U43051.done/wmds_advanceChart.response.json 
-> response_body -> json -> wdv.public.tt_fund_detail_job_record.output_msg
-> endpoint -> advanceChart
-> key1 -> <product_code>
-> batch_name=fund_detail

data_20250813_212612/001_U43051.done/wmds_fundQuoteSummary.response.json  
-> response_body -> json -> wdv.public.tt_fund_detail_job_record.output_msg
-> endpoint -> fundQuoteSummary
-> key1 -> <product_code>
-> batch_name=fund_detail

data_20250813_212612/001_U43051.done/wmds_holdingAllocation.response.json 
-> response_body -> json -> wdv.public.tt_fund_detail_job_record.output_msg
-> endpoint -> holdingAllocation
-> key1 -> <product_code>
-> batch_name=fund_detail

data_20250813_212612/001_U43051.done/wmds_otherFundClasses.response.json 
-> response_body -> json -> wdv.public.tt_fund_detail_job_record.output_msg
-> endpoint -> otherFundClasses
-> key1 -> <product_code>
-> batch_name=fund_detail

data_20250813_212612/001_U43051.done/wmds_quoteDetail.response.json   
-> response_body -> json -> wdv.public.tt_fund_detail_job_record.output_msg
-> endpoint -> quoteDetail
-> key1 -> <product_code>
-> batch_name=fund_detail

data_20250813_212612/001_U43051.done/wmds_topTenHoldings.response.json 
-> response_body -> json -> wdv.public.tt_fund_detail_job_record.output_msg
-> endpoint -> topTenHoldings   
-> key1 -> <product_code>
-> batch_name=fund_detail

读取# /Users/paulo/PycharmProjects/20250809_MCP/08132025_hsbc_fund_screener/data_20250813_212612  下的所有文件, 然后编写py程序, 导入到数据库