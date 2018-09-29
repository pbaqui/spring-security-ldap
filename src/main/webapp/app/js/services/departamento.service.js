/*global app, angular*/
app.service('DepartamentoService', function (GenericService) {
    
    return angular.extend(
        {}, //objeto destino -> resultado
        GenericService, 
        {mapping: app.API_URL_BASE + "departamentos/"}
    );
});
