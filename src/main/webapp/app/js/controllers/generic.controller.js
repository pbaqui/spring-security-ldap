/*global app*/
app.controller('GenericController', function ($scope, $http, $log) {

    $scope.onNewBtnClicked = function () {
        $scope.entity = {};
        $scope.mostrarGuardarBtn = true;
        $scope.mostrarNuevoBtn = false;
        $scope.mostrarBorrarBtn = false;
    };


    $scope.onSaveBtnClicked = function (formularioOk) {
        if (!formularioOk) {
            $scope.errorMsg = "Errores de validacion";
            return;
        }
        var formData = $scope.entity;

        var cargo ={};
        cargo.nombre = formData.nombre;  
        if(formData.id != undefined && formData.id != null){
            cargo.id = formData.id;
        }

        $scope.service.save(JSON.stringify(cargo))
            .then($scope.onSaveSuccess, $scope.onSaveError);
    };

    $scope.loadFormData = function () {
        var fd = new FormData();
        //obtener cada valor de los campos
        for (var property in $scope.entity) {
            var value = $scope.entity[property];
            $log.info(property, value);
            if (value) {
               fd.append(property, value);   
            }
        }
        if ($scope.entity.id) {
            fd.append('id', $scope.entity.id);
        }
        return fd;
    };


    $scope.onSaveSuccess = function (resp) {
        $scope.okMsg = "Registro guardado";
        $scope.entity = resp.data;
          $scope.mostrarGuardarBtn = true;
        $scope.mostrarNuevoBtn = true;
        $scope.mostrarBorrarBtn = true;
        $scope.reloadTable();
        $log.info(resp);
    };

    $scope.onSaveError = function (resp) {
        $scope.errorMsg = "No se pudo guardar registro";
        $log.error(resp);
    };

    $scope.onEditBtnClicked = function (c) {
        $scope.entity = c;
        $scope.mostrarGuardarBtn = true;
        $scope.mostrarNuevoBtn = true;
        $scope.mostrarBorrarBtn = true;
    };

    $scope.onDeleteBtnClicked = function (entity) {
        if (entity) {
            $scope.entity = entity;
        }
        swal({
                title: "Estas seguro?",
                text: "Borrar registro!",
                icon: "warning",
                buttons: true,
                dangerMode: true,
            })
            .then((borradoConfirmado) => {
                if (borradoConfirmado) {
                    $scope.borrar();
                }
            });
    };

    $scope.borrar = function () {
        var id = $scope.entity.id;
        $scope.service.delete(id)
            .then($scope.borradoOk, $scope.borradoError);
    }

    $scope.borradoOk = function () {
        $scope.reloadTable();
        swal("Registro borrado!", {
            icon: "success",
        });
    };

    $scope.borradoError = function (resp) {
        $log.error("Error al borrar:", resp);
        swal(resp.data.errorMsg, {
            icon: "error",
        });
    };

    $scope.reloadTable = function () {
        $scope.loadingList = true;
        $scope.service.list().then(function (resp) {
            $scope.loadingList = false;
            $scope.entities = resp.data;
        });
    };

    //constructor
    (function () {
        $scope.mostrarNuevoBtn = true;
        $scope.reloadTable();
    })();

});
