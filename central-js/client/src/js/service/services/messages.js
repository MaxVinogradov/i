define('messages/service', ['angularAMD'], function (angularAMD) {
	angularAMD.service('MessagesService', ['$http', function($http) {
		this.setMessage = function(message) {
			var data = {
                "sMail": message.sMail,
                "sHead": message.sHead,
                "sBody": message.sBody
            };

            return $http.post('./api/messages', data).then(function(response) {
                // @todo Better notification and error processing.
                alert('Message was sent');
				return response.data;
			});
		};
	}]);
});