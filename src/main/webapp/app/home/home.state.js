(function() {
    'use strict';

    angular
        .module('21PointsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('home', {
            parent: 'app',
            url: '/',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/home/home.html',
                    controller: 'HomeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('home');
                    return $translate.refresh();
                }]
            }
        })
        .state('preferences.add', {
            parent: 'home',
            url: 'add/preferences',
            data: {
                authorities: ['ROLE_USER']
            },
            resolve: {
                mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('home');
                    $translatePartialLoader.addPart('units');
                    $translatePartialLoader.addPart('preferences');
                    return $translate.refresh();
                }]
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/preferences/preferences-dialog.html',
                    controller: 'PreferencesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Preferences', function (Preferences) {
                            return Preferences.user().$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('home', null, { reload: true });
                }, function() {
                    $state.go('home');
                });
            }]
        })
        .state('blood-pressure.add', {
                    parent: 'home',
                    url: '/new',
                    data: {
                        authorities: ['ROLE_USER']
                    },
                    resolve: {
                        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                            $translatePartialLoader.addPart('bloodPressure');
                            $translatePartialLoader.addPart('global');
                            return $translate.refresh();
                        }]
                    },
                    onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                        $uibModal.open({
                            templateUrl: 'app/entities/blood-pressure/blood-pressure-dialog.html',
                            controller: 'BloodPressureDialogController',
                            controllerAs: 'vm',
                            backdrop: 'static',
                            size: 'lg',
                            resolve: {
                                entity: function () {
                                    return {
                                        dateTime: null,
                                        systolic: null,
                                        diastolic: null,
                                        id: null
                                    };
                                }
                            }
                        }).result.then(function() {
                            $state.go('home', null, { reload: true });
                        }, function() {
                            $state.go('home');
                        });
                    }]
                })
        .state('weight.add', {
                    parent: 'home',
                    url: '/add/weight',
                    data: {
                        authorities: ['ROLE_USER']
                    },
                    resolve: {
                        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                            $translatePartialLoader.addPart('weight');
                            $translatePartialLoader.addPart('global');
                            return $translate.refresh();
                        }]
                    },
                    onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                        $uibModal.open({
                            templateUrl: 'app/entities/weight/weight-dialog.html',
                            controller: 'WeightDialogController',
                            controllerAs: 'vm',
                            backdrop: 'static',
                            size: 'lg',
                            resolve: {
                                entity: function () {
                                    return {
                                        dateTime: null,
                                        weight: null,
                                        id: null
                                    };
                                }
                            }
                        }).result.then(function() {
                            $state.go('home', null, { reload: true });
                        }, function() {
                            $state.go('home');
                        });
                    }]
                })
        .state('points.add', {
            parent: 'home',
            url: '/add/points',
            data: {
                authorities: ['ROLE_USER']
            },
            resolve: {
                mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('home');
                    $translatePartialLoader.addPart('points');
                    return $translate.refresh();
                }]
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/points/points-dialog.html',
                    controller: 'PointsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                date: null,
                                exercise: null,
                                meals: null,
                                alcohol: null,
                                notes: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('home', null, { reload: true});
                }, function() {
                    $state.go('home');
                });
            }]
        });
    }
})();
