import axios from 'axios';

// Получаем IP и порт из localStorage
const serverIP = localStorage.getItem('serverIP');
const serverPort = localStorage.getItem('serverPort');

// Динамически формируем baseURL
const baseURL = `http://${serverIP}:${serverPort}/api/v1/operations`;

const api = axios.create({
    baseURL: baseURL,  // Используем динамически сформированный URL
});

// Функции для работы с API
export const getTableValues = async (table) => {
    try {
        const response = await api.get(`/table/${table}`);
        return response.data;
    } catch (error) {
        throw error.response || error;
    }
};

export const createTable = async (table) => {
    try {
        await api.post(`/table/${table}`);
    } catch (error) {
        throw error.response || error;
    }
};

export const deleteTable = async (table) => {
    try {
        await api.delete(`/table/${table}`);
    } catch (error) {
        throw error.response || error;
    }
};

// Можно добавить другие функции для работы с API (getKey, postKey, updateKey, deleteKey)
