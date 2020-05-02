import Vue from 'vue'
import Router from 'vue-router'
import D3Demo from './components/D3Demo.vue'
import HelloWorld from './components/HelloWorld.vue'
import Recorder from './components/Recorder.vue'
import MethodCallRelationGraph from './components/MethodCallRelationGraph.vue'
import MethodCallOrigin from './components/MethodCallOrigin'
import Player from './components/Player.vue'

Vue.use(Router)

export default new Router({
    mode: 'history',
    base: process.env.BASE_URL,
    routes: [
        {
            path: '/',
            name: 'demo',
            component: D3Demo
        },
        {
            path: '/hello',
            name: 'hello',
            component: HelloWorld
        },
        {
            path: '/recorder',
            name: 'recorder',
            component: Recorder
        },
        {
            path: '/method',
            name: 'MethodCallRelationGraph',
            component: MethodCallRelationGraph
        },
        {
            path: '/origin',
            name: 'MethodCallOrigin',
            component: MethodCallOrigin
        },
        {
            path: '/player',
            name: 'Player',
            component: Player
        }
    ]
})
