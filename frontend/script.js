const API_BASE = 'http://localhost:8080/api/v1/electroshop'; // заменить при необходимости

document.getElementById('upload-form').addEventListener('submit', async function (e) {
    e.preventDefault();
    const fileInput = document.getElementById('zip-file');
    const file = fileInput.files[0];

    if (!file) return;

    const formData = new FormData();
    formData.append('file', file);

    const statusDiv = document.getElementById('upload-status');
    statusDiv.textContent = 'Загрузка...';

    try {
        const response = await fetch(`${API_BASE}/upload`, {
            method: 'POST',
            body: formData
        });

        if (response.ok) {
            statusDiv.textContent = 'Загрузка завершена. Обновление данных...';
            loadAllTables(); // повторно загружаем все таблицы
        } else {
            const text = await response.text();
            statusDiv.textContent = `Ошибка: ${text}`;
        }
    } catch (err) {
        statusDiv.textContent = 'Ошибка при загрузке ZIP: ' + err.message;
    }
});

function loadAllTables() {
    // 1. Лучшие сотрудники
    fetch(`${API_BASE}/top-employees`)
        .then(r => r.json())
        .then(data => {
            const tbody = document.querySelector('#top-employees-table tbody');
            tbody.innerHTML = '';
            data.forEach(seller => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${seller.lastName} ${seller.firstName} ${seller.patronymic}</td>
                    <td>${seller.positionTypeName}</td>
                    <td>${seller.gender ? 'Муж' : 'Жен'}</td>
                    <td>${seller.soldItemsQuantity}</td>
                    <td>${seller.soldItemsSum}</td>
                `;
                tbody.appendChild(row);
            });
        });

    // 3. Доход с оплат наличными
    fetch(`${API_BASE}/revenue?purchaseType=Наличные`)
        .then(r => r.text())
        .then(income => {
            document.getElementById('cash-income').innerText = `Итого: ${income} руб.`;
        });
}

// загружаем при загрузке страницы
loadAllTables();

// 2. Лучшие продавцы по должности и типу товаров
document.getElementById('top-sellers-form').addEventListener('submit', function (e) {
    e.preventDefault();
    const position = document.getElementById('position-input').value;
    const type = document.getElementById('type-input').value;

    fetch(`${API_BASE}/top-seller?positionName=${encodeURIComponent(position)}&electroTypeName=${encodeURIComponent(type)}`)
        .then(r => r.json())
        .then(data => {
            const tbody = document.querySelector('#top-sellers-table tbody');
            tbody.innerHTML = '';
            data.forEach(seller => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${seller.lastName} ${seller.firstName} ${seller.patronymic}</td>
                    <td>${seller.positionTypeName}</td>
                    <td>${seller.gender ? 'Муж' : 'Жен'}</td>
                    <td>${seller.soldItemsQuantity}</td>
                    <td>${seller.soldItemsSum}</td>
                    <td>${seller.electroTypeName}</td>
                `;
                tbody.appendChild(row);
            });
        });
});



