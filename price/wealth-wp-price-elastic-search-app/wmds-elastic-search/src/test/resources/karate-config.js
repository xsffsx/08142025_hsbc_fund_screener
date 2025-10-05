function fn() {

	config = {
		// Don't use localhost in digital pipeline build, seems even pipeline create test springboot app, it can not use proxy when localhost calling other apis
		// seems proxy setting in katate test only effect the feature url only
		//baseUrl : 'http://localhost:8099/'
		baseUrl : 'https://mds-elastic-search-debug.wealth-platform-amh.dev.aws.cloud.hhhh/'
	};

	karate.log('karate.config: ', config);
	return config;
}
