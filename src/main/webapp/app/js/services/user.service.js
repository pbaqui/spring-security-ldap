/*global angular, app, FormData*/

app.service('UserService', ['$http', function ($http) {

    return {
        config: {
            headers: {
                'Content-Type': undefined
            },
            transformRequest: angular.identity
        },
        
        login: function (username, password) {
           var fd = new FormData();
           fd.append('username', username);
           fd.append('password', password);   
         
            return $http.post(app.API_URL_BASE + "login", fd, this.config);
        },
        
        logout: function () {
            return $http.post(app.API_URL_BASE + "cerrar-sesion");
        },
        
        sesionInfo: function () {
            return $http.get(app.API_URL_BASE + "sesion/info");
        }
        
    };
}]);
