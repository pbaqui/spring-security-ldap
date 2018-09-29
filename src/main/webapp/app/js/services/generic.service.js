/*global app, angular*/
app.service('GenericService', function ($http, $log) {
    
    return {
        config: {
            headers: {
                'Content-Type': undefined
            },
            transformRequest: angular.identity
        },
        
        list: function () {
            return $http.get(this.mapping);
        },
        
        save: function (formData) {
            return $http.post(this.mapping, formData, this.config);
        },
        
        delete: function(id){
            $log.info("Borrando registro con id:", id);
            return $http.delete(this.mapping + id);
        }
    };
});
