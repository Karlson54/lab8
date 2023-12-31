# Аналіз Погоди

## Функціональність програми

Програма "Аналіз Погоди" призначена для отримання та аналізу історичних даних про температуру та опади за певний період часу. Основна мета - виведення інформації про найтепліші дні та дні з найвищими опадами. Програма дозволяє користувачеві отримати важливі статистичні дані для подальших аналітичних висновків.

## Опис роботи

Програма використовує відкрите API для прогнозу погоди (Open-Meteo API). Для аналізу температури та кількості опадів використовуються дві окремі класи - `TemperatureAnalysis` та `PrecipitationAnalysis`. Кожен клас використовує HTTP-запити для отримання даних з API, які подаються у вигляді JSON. Дані аналізуються та виводяться користувачеві.

### Клас TemperatureAnalysis

Клас `TemperatureAnalysis` реалізує інтерфейс `Runnable` та відповідає за аналіз температурних даних. При створенні екземпляру класу виконується запит до API, отримуються дані про температуру, які подаються у вигляді списку `DataEntry`. Список сортується в порядку зменшення температури, та виводиться інформація про топ-10 найтепліших днів.

### Клас PrecipitationAnalysis

Клас `PrecipitationAnalysis` реалізує інтерфейс `Runnable` та відповідає за аналіз даних про опади. При створенні екземпляру класу виконується запит до API, отримуються дані про опади, які подаються у вигляді списку `DataEntry`. Список сортується в порядку зменшення опадів, та виводиться інформація про топ-10 днів з найвищою кількістю опадів. Також в програмі здійснюється пошук періодів, коли протягом 7 днів йшов дощ, та виводиться відповідне повідомлення.

### API Опис

- **URL для температури:**
  `https://archive-api.open-meteo.com/v1/archive?latitude=50.45466&longitude=30.5238&start_date=2023-01-01&end_date=2023-12-26&daily=temperature_2m_max`

- **URL для опадів:**
  `https://archive-api.open-meteo.com/v1/archive?latitude=50.45466&longitude=30.5238&start_date=2023-01-01&end_date=2023-12-26&daily=precipitation_sum`

- **Методи:**
  - **GET:** Використовується для отримання даних за певний період часу та параметрів.

- **Сутності:**
  - **DataEntry:** Об'єкт, який представляє запис даних (час і значення температури або кількості опадів).

### "Five Why's" Принцип

1. **Чому було вирішено створити програму?**
   - Для аналізу історичних погодних даних та отримання статистики.

2. **Чому використовується Open-Meteo API?**
   - API надає доступ до даних погоди, що дозволяє отримувати достовірну інформацію.

3. **Чому використовуються окремі класи для температури та опадів?**
   - Для розділення відповідальностей та полегшення розширення функціоналу.

4. **Чому виводяться топ-10 днів з найвищою температурою та опадами?**
   - Для наглядності та швидкого отримання ключової інформації.

5. **Чому використовуються тести?**
   - Для перевірки правильності роботи програми та забезпечення її стабільності.

## Тести

Тести перевіряють правильність аналізу температури та опадів. Вони включають такі тест-кейси:

- Перевірка, чи дані про температуру та опади не порожні.
- Перевірка, чи дані про температуру та опади відсортовані в порядку зменшення.

Для запуску тестів використовуйте інструкцію в розділі "Тести" у README.