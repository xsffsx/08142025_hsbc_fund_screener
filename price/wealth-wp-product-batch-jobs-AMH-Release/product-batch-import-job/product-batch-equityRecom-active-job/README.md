# Reference page:
* https://wpb-confluence.systems.uk.dummy/display/WWS/AMH+Equity+Recommendations+Solution

# Step:
1. Read active list excel file (Daily);
2. Get other EquityRecommendations fields from collection 'equity_recommendations' base on Reuters. 
3. If not match any coverage records, will not update to product.
4. if match one coverage record and match products base on Reuters, 15 equityRecommendations fields should insert into product collection.