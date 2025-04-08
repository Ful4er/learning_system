document.addEventListener('DOMContentLoaded', function() {
    const authApp = {
        init() {
            this.cacheElements();
            this.bindEvents();
            this.setActiveForm('login');
        },

        cacheElements() {
            this.loginForm = document.getElementById('login-form');
            this.registerForm = document.getElementById('register-form');
            this.loginToggle = document.querySelector('[data-form="login"]');
            this.registerToggle = document.querySelector('[data-form="register"]');
            this.subtitle = document.getElementById('auth-subtitle');
            this.footerText = document.getElementById('auth-footer-text');
            this.switchLink = document.getElementById('auth-switch-link');
        },

        bindEvents() {
            this.loginToggle.addEventListener('click', () => this.setActiveForm('login'));
            this.registerToggle.addEventListener('click', () => this.setActiveForm('register'));
            this.switchLink.addEventListener('click', (e) => {
                e.preventDefault();
                this.toggleForms();
            });
        },

        setActiveForm(formType) {
            const isLogin = formType === 'login';

            // Update forms visibility
            this.loginForm.classList.toggle('hidden', !isLogin);
            this.registerForm.classList.toggle('hidden', isLogin);

            // Update toggle buttons
            this.loginToggle.classList.toggle('active', isLogin);
            this.registerToggle.classList.toggle('active', !isLogin);

            // Update texts
            this.subtitle.textContent = isLogin ? 'Sign in to your account' : 'Create a new account';
            this.footerText.textContent = isLogin ? "Don't have an account?" : "Already have an account?";
            this.switchLink.textContent = isLogin ? "Register" : "Sign in";
        },

        toggleForms() {
            const isLoginVisible = this.loginForm.classList.contains('hidden');
            this.setActiveForm(isLoginVisible ? 'login' : 'register');
        }
    };

    authApp.init();
});