/* global app,  localStorage*/

app.controller('LoginController', function ($rootScope, $scope, $log, $location, $controller, UserService) {
    $scope.username = 'admin';
    $scope.password = 'adm';


    $scope.loginOk = function (response) {
        $scope.errorMsg = null;
        $log.info(response);
        $rootScope.usuario = response.data.usuario;
        localStorage.setItem('usuario', JSON.stringify($rootScope.usuario));
        $location.path('/');
    };

    $scope.loginError = function (response) {
        $log.error(response);
        if (response.status == 401) {
            $scope.errorMsg = 'Usuario o contraseña incorrectos';
        } else {
            $scope.errorMsg = 'Error al iniciar sesion';
        }

    };

    $scope.login = function (formIsValid) {

        if (!formIsValid) {
            $scope.errorMsg = "Ingrese usuario y contraseña";
            return;
        }
        $log.info("Iniciando sesion: ", $scope.username);
        UserService.login($scope.username, $scope.password).then($scope.loginOk, $scope.loginError);
    };

    $scope.checkSession = function () {
        localStorage.setItem('usuario', null);
        UserService.sesionInfo().then(function (response) {
            $log.info("SesionInfo:", response.data);
            if (response.data.usuario) {
                $log.info("Usuario ya inició sesión:", response.data);
                $rootScope.usuario = response.data.usuario;
                localStorage.setItem('usuario', JSON.stringify($rootScope.usuario));
                $location.path('/');
            }
        });
    };

    (function () {
        $scope.checkSession();
    })();

});


app.controller('NavController', function ($scope, $log, $rootScope, $route, UserService) {

    $scope.logout = function () {
        UserService.logout().then(function (response) {
            $log.info("logout", response);
            localStorage.setItem('usuario', null);
            $rootScope.usuario = null;
            $route.reload();
        }, function (response) {
            $log.error(response);
        });
    };

    (function () {
        if ($rootScope.usuario == null) {
            try {
                $rootScope.usuario = JSON.parse(localStorage.getItem('usuario'));
            } catch (err) {
                $rootScope.usuario = {};
            }
        }

    })();
});
