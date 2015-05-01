define('service.country.built-in', ['angularAMD'], function (angularAMD) {
    var app = angular.module('service.country.built-in', []);

    app.config(['$stateProvider', function ($stateProvider) {
        $stateProvider
            .state('service.country.built-in', {
                url: '/built-in',
                views: {
                    '': angularAMD.route({
                        templateProvider: ['$templateCache', function($templateCache) {
							return $templateCache.get('html/service/country/built-in/index.html');
						}],
						controller: 'ServiceBuiltInController',
                        controllerUrl: 'service/built-in/controller'
                    })
                }
            })
			.state('service.country.built-in.bankid', {
				url: '/built-in/?code',
				parent: 'service.country',
				resolve: {
					BankIDLogin: ['$q', '$state', '$location', '$stateParams', 'BankIDService', function($q, $state, $location, $stateParams, BankIDService) {
						var url = $location.protocol()
							+'://'
							+$location.host()
							+':'
							+$location.port()
							+$state.href('service.country.built-in.bankid', { id: $stateParams.id });
						
						return BankIDService.login($stateParams.code, url).then(function(data) {
							return data.hasOwnProperty('error') ? $q.reject(null): data;
						});
					}],
					BankIDAccount: ['BankIDService', 'BankIDLogin', function(BankIDService, BankIDLogin) {
						return BankIDService.account(BankIDLogin.access_token);
					}]
				},
				views: {
					'@service.country': angularAMD.route({
                        templateProvider: ['$templateCache', function($templateCache) {
							return $templateCache.get('html/service/country/built-in/login.html');
						}],
						controller: 'ServiceBuiltInBankIDController',
						controllerUrl: 'service/built-in/bankid/controller'
					})
				}
			})
    }]);
    return app;
});

