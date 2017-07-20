/**
 * Created by william on 7/18/17.
 */
(function () {
    'use strict';
    angular.module('tasksApp')
        .factory('Email', Email);

    Email.$inject = ['$resource'];

    function Email($resource) {
        return $resource('api/email', {}, {
            'sendEmail': {
                method: 'POST'
            }
        });
    }
})();
