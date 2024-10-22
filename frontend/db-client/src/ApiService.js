import axios from 'axios';

const getBaseURL = () => {
    const serverIP = localStorage.getItem('serverIP');
    const serverPort = localStorage.getItem('serverPort');
    return `http://${serverIP}:${serverPort}/api/v1/operations`;
};

const createApiInstance = () => {
    return axios.create({
        baseURL: getBaseURL(),
    });
};

export const getTableValues = async (table) => {
    const api = createApiInstance();
    try {
        const response = await api.get(`/table/${table}`);
        return response.data;
    } catch (error) {
        throw error.response || error;
    }
};

export const createTable = async (table) => {
    const api = createApiInstance();
    try {
        await api.post(`/table/${table}`);
    } catch (error) {
        throw error.response || error;
    }
};

export const deleteTable = async (table) => {
    const api = createApiInstance();
    try {
        await api.delete(`/table/${table}`);
    } catch (error) {
        throw error.response || error;
    }
};

export const getAllTables = async () => {
    const api = createApiInstance();
    try {
        const response = await api.get('/tables');
        return response.data;
    } catch (error) {
        throw error.response || error;
    }
};
