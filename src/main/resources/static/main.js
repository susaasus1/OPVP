// Создание таблицы
document.getElementById('create-table-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const tableName = document.getElementById('table-name').value;

    try {
        const response = await fetch(`http://localhost:8080/api/v1/operations/table/${tableName}`, {
            method: 'POST',
        });
            alert(`Table "${tableName}" created successfully!`);

    } catch (error) {
        console.error('Error creating table:', error);
    }
});

function loadTableData(tableName) {
    fetch(`http://localhost:8080/api/v1/operations/table/${tableName}`, {
    })
        .then(response => {
            console.log(response)
            if (response.ok) {
                console.log(response.text())
                return response.text();// Возвращаем текст ответа
            } else {
                console.error('Failed to load data:', response.status);
                throw new Error('Network response was not ok');
            }
        })
        .then(text => {
            if (text) {
                const data = JSON.parse(text);  // Преобразуем текст в JSON

                // Теперь можно пройтись по данным
                data.forEach(item => {
                    console.log(item);  // Выводим каждый элемент массива
                });

                // Отобразить данные в таблице
                renderTableData(data);
            } else {
                console.warn('Response body is empty.');
            }
        })
        .catch(error => {
            console.error('Error loading table data:', error);
        });
}

// Отображение данных в таблице
function renderTableData(data) {
    const tableBody = document.getElementById('table-body');
    tableBody.innerHTML = '';  // Очищаем таблицу перед добавлением новых строк

    data.forEach(item => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${item}</td>  <!-- Вывод каждого элемента массива -->
        `;
        tableBody.appendChild(tr);
    });
}

// Удаление значения по ключу
async function deleteKey(key) {
    const tableName = document.getElementById('table-name').value; // Получаем имя текущей таблицы
    try {
        const response = await fetch(`http://localhost:8080/api/v1/operations/table/${tableName}/key/${key}`, {
            method: 'DELETE',
        });

            alert(`Key "${key}" deleted successfully.`);
            loadTableData(tableName); // Перезагружаем таблицу
    } catch (error) {
        console.error('Error deleting key:', error);
    }
}

// Добавление данных по ключу в таблицу
document.getElementById('add-data-form').addEventListener('submit', async (e) => {
    e.preventDefault();

    const tableName = document.getElementById('table-name2').value;  // Получаем имя таблицы
    const key = document.getElementById('key').value;  // Получаем ключ
    const value = document.getElementById('value').value;  // Получаем значение

    try {
        const response = await fetch(`http://localhost:8080/api/v1/operations/table/${tableName}/key/${key}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(value)
        });

        if (response.status === 204) {
            alert(`Data with key "${key}" added successfully!`);
            loadTableData(tableName);  // Перезагружаем таблицу для отображения нового значения
        } else if (response.status === 409) {
            alert('Key already exists.');
        }
    } catch (error) {
        console.error('Error adding data:', error);
    }
});


// Инициализация таблицы (по умолчанию загружаем данные таблицы при старте)
document.addEventListener('DOMContentLoaded', () => {
    const defaultTableName = 'test'; // Название таблицы по умолчанию
    loadTableData(defaultTableName);
});
