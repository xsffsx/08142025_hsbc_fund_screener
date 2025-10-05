function fn() {
    var uuid = '' + java.util.UUID.randomUUID(); // convert to string
    return {
        'Content-Type': 'application/json; charset=utf-8',
        'Accept': 'application/json',
        'X-hhhh-Locale':'en_US',
        'X-hhhh-Chnl-CountryCode': 'HK',
        'X-hhhh-Channel-Id': 'OHI',
        'X-hhhh-Chnl-Group-Member': 'HBAP',
        'X-hhhh-App-Code': 'WD',
        'Authorization': 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzZWN1cmVUb2tlbiI6Ik9Bcy9aNkRwZkFRY1pGYXJyeGtsNFVrK25pZDh0N2JCMllHeEUvTktWcTNaQlR6WVl4UTNaMG1iV0xKSUI1MUJOVjJ4NzlTMlI4SXhSTVpsd00rclNaZkdpeVEyTEgzeWszRGhlL25RS0p4Y0UvbFcyR0d5RmpLdWs1ckdQcVRUTVB4ZUVtQlY3WmJvNEk3b3Bqa1hCRzAxT3lXYkduTHdXZnpHMjAzRWdNcTB4bzVCdktvcjA0U1dYV0cxK3lJRE1WaDdEY2t0WmIvQVRvTk1uWEthclE9PSIsImZyYXVkU2Vzc2lvbklkIjoiNDE5MjEwNTc4NTM0NTcyNjkxNzM3MjY2Nzg0NjU5MjMwNjE0ODMwMjkyODU2MDc2ODI5MTAzNjE3NjAyODI0MTYyNDU1MTYyOTM4MjA5MTY1MjMxMSJ9.6m11sZgmmBTwJLn-iSHD5WezFCVpqSOBxk-pAMbdwy8',
        'X-hhhh-Request-Correlation-Id': uuid,

    };
}