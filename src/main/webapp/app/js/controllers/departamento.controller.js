/*global app, angular*/
app.controller('DepartamentoController', function ($scope, $log, DepartamentoService, $controller) {

    $scope.service = DepartamentoService;

    angular.extend(this, $controller('GenericController', {
        $scope: $scope
    }));
});
