(function() {
    'use strict';
    angular
        .module('21PointsApp')
        .factory('BloodPressure', BloodPressure);

    BloodPressure.$inject = ['$resource', 'DateUtils'];

    function BloodPressure ($resource, DateUtils) {
        var resourceUrl =  'api/blood-pressures/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'last30Days': {method: 'GET', isArray: false, url: 'api/bp-by-days/30'},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateTime = DateUtils.convertDateTimeFromServer(data.dateTime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
