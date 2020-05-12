/**
 * @Author: Shine
 * @Date: 2019/3/18
 */

import fetch from '@/utils/fetch'

export function getUploadedFileList(data) {
    return fetch({
        url: '/apiurl/allVersion',
        method: 'get',
        paras:data
    })
}

export function getRelationByFileName(name, packages, packagesToCall) {
    return fetch({
        url: '/apiurl/relation',
        method: 'get',
        params: {
            name: name,
            packages: packages,
            packagesToCall: packagesToCall
        }
    })
}

export function getTestCaseList() {
    return fetch({
        url: '/apiurl/testCaseList',
        method: 'get'
    })
}

export function runTestCase(projectname, testcasename, method) {
    return fetch({
        url: '/apiurl/runTestCase',
        method: 'get',
        params: {
            projectname: projectname,
            testcasename: testcasename,
            method: method
        }
    })
}

export function getTestRunningStatus(taskId) {
    return fetch({
        url: '/apiurl/getInvokingProcess',
        method: 'get',
        params: {
            key: taskId
        }
    })
}

export function getInvokingResults(task_id_Key) {
    return fetch({
        url: '/apiurl/getInvokingResults',
        method: 'get',
        params: {
            key: task_id_Key
        }
    })
}

export function postRegression(params) {
    return fetch({
        url: '/apiurl/regressiontest',
        method: 'get',
        params: params
    })
}

export function uploadRecord(events) {
    const body = JSON.stringify({ events });
    console.log(events)
    return fetch({
        url: '/apiurl/record',
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body
    });
}

export function createVersion(data) {
    return fetch({
        url: '/apiurl/createVersion',
        method: "POST",
        headers: {
            "Content-Type": "multipart/form-data"
        },
        data
    });
}

export function getAllProj(){
    return fetch({
        url: '/apiurl/allProject',
        method: 'get'
    })
}

export function createNewProj(data){
    return fetch({
        url: '/apiurl/createProject',
        method: 'get',
        params: data
    })
}

export function deleteProject(data){
    return fetch({
        url: '/apiurl/deleteProject',
        method: 'DELETE',
        params: data
    })
}

export function getAllVersion(data){
    return fetch({
        url:'/apiurl/allVersion',
        method:'get',
        params: data
    })
}

export function deleteVersion(data){
    return fetch({
        url: '/apiurl/deleteVersion',
        method: 'DELETE',
        params: data
    })
}