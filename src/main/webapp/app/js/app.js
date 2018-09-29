/*global angular*/
var app = angular.module('testApp', ['ngRoute']);
app.API_URL_BASE = "http://localhost:8080/security-ldap/";


/*
Si la petición retorna 201, falta autenticar. Si retorna status < 0, se muestra página de error*/
app.service('authInterceptor', function ($q, $window, $log) {
    var service = this;
    service.responseError = function (response) {
        if (response.status == 401) {
            $window.location.href = "#/login";
        } else if (response.status == 403) {
            $window.location.href = "#/acceso-denegado";
        } else if (response.status < 0) {
            $log.error(response);
            $window.location.href = "#/error";
        }
        return $q.reject(response);
    };
});

app.config(function ($httpProvider) {
    $httpProvider.defaults.withCredentials = true;
    $httpProvider.interceptors.push('authInterceptor');
});


app.config(function ($routeProvider, $locationProvider) {

    //evitar '!' en la url
    $locationProvider.hashPrefix('');
    $routeProvider
        .when('/login', {
            templateUrl: 'vistas/login/login.html'
        })
        .when('/', {
            templateUrl: 'vistas/tarea/tareas_pendientes.html'
        })
        .when('/acceso-denegado', {
            templateUrl: 'vistas/acceso_denegado.html'
        })
        .when('/solicitantes', {
            templateUrl: 'vistas/solicitante/solicitante.html'
        })
        .when('/cargos', {
            templateUrl: 'vistas/cargo/cargo.html'
        })
        .when('/departamentos', {
            templateUrl: 'vistas/departamento/departamento.html'
        })
        .when('/tareas-pendientes', {
            templateUrl: 'vistas/tarea/tareas_pendientes.html'
        })
        .when('/tareas-finalizadas', {
            templateUrl: 'vistas/tarea/tareas_finalizadas.html'
        });
});