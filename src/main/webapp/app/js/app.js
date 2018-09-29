/**Global Angular */
var app = angular.module('testApp', ['ngRoute']);

app.config(function ($routeProvider, $locationProvider) {

    //evitar '!' en la url
    $locationProvider.hashPrefix('');
    $routeProvider
        .when("/login", {
            templateUrl: 'vistas/login/login.html'
        })
        .when('/solicitante', {
            templateUrl: 'vistas/solicitante/solicitante.html'
        })
        .when('/cargo', {
            templateUrl: 'vistas/cargo/cargo.html'
        })
        .when('/departamento', {
            templateUrl: 'vistas/departamento/departamento.html'
        })
        .otherwise({
            redirect: '/'
        });

});
