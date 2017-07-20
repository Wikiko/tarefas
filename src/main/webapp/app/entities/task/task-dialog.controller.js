(function() {
    'use strict';

    angular
        .module('tasksApp')
        .controller('TaskDialogController', TaskDialogController);

    TaskDialogController.$inject = ['$timeout', '$scope', '$log', '$stateParams', '$uibModalInstance', 'entity', 'Task', 'User', 'Email'];

    function TaskDialogController ($timeout, $scope, $log, $stateParams, $uibModalInstance, entity, Task, User, Email) {
        var vm = this;

        vm.task = entity;
        vm.clear = clear;
        vm.save = save;
        vm.checkSendEmail = checkSendEmail;
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.task.id !== null) {
                Task.update(vm.task, onSaveSuccess, onSaveError);
            } else {
                Task.save(vm.task, onSaveSuccess, onSaveError);
            }
        }

        function checkSendEmail() {
            save();
            if (vm.task.sendEmail === true) {
                var result = Email.sendEmail(vm.task);
                $log.log(result);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tasksApp:taskUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
