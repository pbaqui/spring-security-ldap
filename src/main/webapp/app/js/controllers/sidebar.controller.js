/* global app*/

app.controller('SidebarController', function($scope, $log){
    
    $scope.setActive = function(item){
        $scope.item = item;
        $log.info("Item:", item);
    };
    
    $scope.isActive = function(item){
        return $scope.item == item;
    };
});