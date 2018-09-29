/*global app, angular*/
app.controller('CargoController', function ($scope, $log, CargoService, $controller) {

    $scope.service = CargoService;

    angular.extend(this, $controller('GenericController', {
        $scope: $scope
    }));
});
