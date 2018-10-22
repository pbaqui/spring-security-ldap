/*global app, angular*/
app.service('CargoService', function (GenericService) {
    
    return angular.extend(
        {}, //objeto destino -> resultado
        GenericService, 
        {mapping: app.API_URL_BASE + "cargo/"}
    );
});
