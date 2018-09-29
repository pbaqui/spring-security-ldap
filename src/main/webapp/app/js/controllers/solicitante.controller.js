/*global app, FormData*/
app.controller('SolicitanteController', function ($scope, $http, CargoService, DepartamentoService, SolicitanteService, $log) {

    CargoService.list().then(function (resp) {
        $scope.cargos = resp.data;
    });

    DepartamentoService.list().then(function (resp) {
        $scope.departamentos = resp.data;
    });

    $scope.cargarSolicitantes = function () {
        SolicitanteService.list().then(function (resp) {
            $scope.solicitantes = resp.data;
        });
    };

    $scope.isAuditor = function (){
        $scope.service.isAuditor().then(function(resp){
            $scope.esAuditor= resp.data; 
        });
    }

    $scope.guardar = function (formIsValid) {
        if (!formIsValid) {
            $scope.errorMsg = "Complete los campos";
            return;
        }
        $log.info("Cargando FormData:", $scope.solicitante);
        var data = new FormData();
        data.append('cedula', $scope.solicitante.cedula);
        data.append('nombre', $scope.solicitante.nombre);
        data.append('apellido', $scope.solicitante.apellido);
        data.append('cargo.id', $scope.solicitante.cargo);
        data.append('departamento.id', $scope.solicitante.departamento);
    
        
        SolicitanteService.save(data).then(
            function (resp) {
                $scope.errorMsg = null;
                $log.info("Registro guardado:", resp); 
                //actualizar tabla
                $scope.cargarSolicitantes();
            },
            function (resp) {
                $scope.errorMsg = resp.statusText;
                $log.error(resp);
            }
        );
    };
    
    (function(){
        $scope.cargarSolicitantes();
    })();
});
