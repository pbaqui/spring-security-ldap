/*global app, angular*/
app.service('SolicitanteService', function (GenericService) {
    
    return angular.extend(
        {}, //objeto destino -> resultado
        GenericService, 
        {mapping: app.API_URL_BASE + "solicitante/"}
    );
});
