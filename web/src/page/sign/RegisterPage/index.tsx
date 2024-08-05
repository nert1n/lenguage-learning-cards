'use client';

import Link from 'next/link';
import { useForm } from 'react-hook-form';
import { useMutation } from 'react-query';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { useRouter } from 'next/navigation';
import { useEffect, useState } from 'react';

import AuthService from '@/services/auth.service';
import MainText from '@/components/model/ui/MainText';
import MainTitle from '@/components/model/ui/MainTitle';
import MainButton from '@/components/model/ui/MainButton';
import MainInput from '@/components/model/ui/MainInput';
import useAuthStore from '@/store/useAuthStore';
import IRegisterPage from '@/page/sign/RegisterPage/register-page.interface';
import ILoginPage from '@/page/sign/LoginPage/login-page.interface';

import styles from './RegisterPage.module.scss';

function RegisterPage() {
	const {
		register,
		formState: { errors },
		handleSubmit,
	} = useForm<IRegisterPage>({ mode: 'onBlur' });
	const [username, setUsername] = useState<string>('');
	const [password, setPassword] = useState<string>('');
	const router = useRouter();
	const { setJwt } = useAuthStore(({ setJwt }) => ({ setJwt }));

	const mutationRegister = useMutation(
		({ username, password, email }: IRegisterPage) =>
			AuthService.postRegister(username, password, email),
	);

	const mutationLogin = useMutation(({ username, password }: ILoginPage) =>
		AuthService.postLogin(username, password),
	);

	if (Object.values(errors)[0]?.message) {
		toast.error(Object.values(errors)[0].message);
	}

	useEffect(() => {
		if (!mutationRegister.isSuccess) return;
		try {
			mutationLogin.mutateAsync({ username, password });
		} catch {
			toast.error('Ошибка регистрации пользователя!');
		}
	}, [mutationLogin, mutationRegister.isSuccess, password, username]);

	useEffect(() => {
		if (!mutationLogin.isSuccess) return;
		setJwt(mutationLogin.data.data);
		router.push('/home');
	}, [mutationLogin, mutationLogin.isSuccess, router, setJwt]);

	const onSubmit = async ({
		username,
		password,
		email,
		repeatPassword,
	}: IRegisterPage) => {
		setPassword(password);

		if (password !== repeatPassword) {
			toast.error('Пароли не совпадают');
			return;
		}

		try {
			await mutationRegister.mutateAsync({
				username,
				email,
				password,
				repeatPassword,
			});
		} catch (err) {
			toast('Пользователь с таким именем или почтой уже существует');
		}
	};

	return (
		<form className={styles.container} onSubmit={handleSubmit(onSubmit)}>
			<ToastContainer
				position="top-center"
				autoClose={5000}
				hideProgressBar={false}
				newestOnTop={false}
				closeOnClick
				rtl={false}
				pauseOnFocusLoss
				draggable
				pauseOnHover
				theme="light"
			/>
			<MainTitle size="large">Регистрация</MainTitle>
			<MainInput
				placeholder="Ваше имя"
				type="text"
				className={`${styles.input} ${errors?.username && styles.error}`}
				{...register('username', {
					required: 'Пожалуйста введите имя пользователя!',
					minLength: {
						value: 3,
						message: 'Имя должно быть не короче 3 букв!',
					},
					maxLength: {
						value: 36,
						message: 'Имя должно быть короче',
					},
				})}
			/>
			<MainInput
				placeholder="Почта"
				type="email"
				className={`${styles.input} ${errors?.email && styles.error}`}
				{...register('email', {
					required: 'Пожалуйста введите почту пользователя!',
					minLength: {
						value: 5,
						message: 'Электронная почта слишком короткая!',
					},
					maxLength: {
						value: 36,
						message: 'Электронная почта слишком длинная!',
					},
					validate: {
						noRussianLetters: value =>
							!/[А-яЁё]/.test(value) ||
							'Электронная почта не должна содержать русские буквы!',
						validFormat: value =>
							/^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/i.test(value) ||
							'Неверный формат электронной почты!',
					},
				})}
			/>
			<MainInput
				placeholder="Пароль"
				type="password"
				className={`${styles.input} ${errors?.password && styles.error}`}
				{...register('password', {
					required: 'Пожалуйста введите пароль пользователя!',
					minLength: {
						value: 5,
						message: 'Пароль должен быть не короче 5 букв!',
					},
					maxLength: {
						value: 36,
						message: 'Пароль слишком длинный!',
					},
					validate: {
						noRussianLetters: value =>
							!/[А-яЁё]/.test(value) ||
							'Пароль не должен содержать русские буквы!',
						hasUpperCase: value =>
							/[A-Z]/.test(value) ||
							'Пароль должен содержать хотя бы одну заглавную букву!',
						hasNumber: value =>
							/\d/.test(value) || 'Пароль должен содержать хотя бы одну цифру!',
					},
				})}
			/>
			<MainInput
				placeholder="Подтверждение пароля"
				type="password"
				className={`${styles.input} ${errors?.repeatPassword && styles.error}`}
				{...register('repeatPassword', {
					required: 'Пожалуйста введите пароль пользователя!',
					minLength: {
						value: 5,
						message: 'Пароль должен быть не короче 5 букв!',
					},
					maxLength: {
						value: 36,
						message: 'Пароль слишком длинный!',
					},
					validate: {
						noRussianLetters: value =>
							!/[А-яЁё]/.test(value) ||
							'Пароль не должен содержать русские буквы!',
						hasUpperCase: value =>
							/[A-Z]/.test(value) ||
							'Пароль должен содержать хотя бы одну заглавную букву!',
						hasNumber: value =>
							/\d/.test(value) || 'Пароль должен содержать хотя бы одну цифру!',
					},
				})}
			/>
			<MainButton
				size="large"
				type="submit"
				color="blue"
				className={styles.btn}>
				Зарегистрироваться
			</MainButton>
			<div className={styles['under-btn-text']}>
				<MainText type="regular" size="extra-small">
					Уже есть аккаунт?
				</MainText>
				<MainText type="regular" size="extra-small" color="blue">
					<Link href="/login">Авторизуйтесь!</Link>
				</MainText>
			</div>
		</form>
	);
}

export default RegisterPage;
