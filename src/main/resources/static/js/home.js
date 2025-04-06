function authApp() {
    return {
        isLogin: true,
        handleLogin(event) {
            event.preventDefault(); // Предотвращаем стандартное поведение формы
            const form = event.target; // Получаем форму
            form.submit(); // Отправляем данные на сервер
        },
        handleRegister(event) {
            event.preventDefault(); // Предотвращаем стандартное поведение формы
            const form = event.target; // Получаем форму
            form.submit(); // Отправляем данные на сервер
        }
    };
}