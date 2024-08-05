'use client';

import Link from 'next/link';
import { useForm } from 'react-hook-form';
import { useMutation } from 'react-query';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { useRouter } from 'next/navigation';

import useAuthStore from '@/store/useAuthStore';
import MainButton from '@/components/model/ui/MainButton';
import MainInput from '@/components/model/ui/MainInput';
import MainTitle from '@/components/model/ui/MainTitle';
import MainText from '@/components/model/ui/MainText';
import AuthService from '@/services/auth.service';
import ILoginPage from '@/page/sign/LoginPage/login-page.interface';

import styles from './LoginPage.module.scss';

function LoginPage() {
	const {
		register,
		formState: { errors },
		handleSubmit,
	} = useForm<ILoginPage>({ mode: 'onBlur' });
	const setJwt = useAuthStore(state => state.setJwt);
	const router = useRouter();
	const mutation = useMutation(({ username, password }: ILoginPage) =>
		AuthService.postLogin(username, password),
	);

	if (Object.values(errors)[0]?.message) {
		toast.error(Object.values(errors)[0].message);
	}

	const onSubmit = async ({ username, password }: ILoginPage) => {
		try {
			const result = (await mutation.mutateAsync({ username, password })).data;
			if (result !== undefined) setJwt(result);
			router.push('/home');
		} catch (err) {
			toast('Неверное имя или пароль!');
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
			<MainTitle size="large">Вход</MainTitle>
			<MainInput
				className={`${styles.auth__input} ${errors?.username && styles.error}`}
				placeholder="Имя пользователя"
				type="text"
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
				className={`${styles.auth__input} ${errors?.password && styles.error}`}
				placeholder="Пароль"
				type="password"
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
			<MainText
				color="blue"
				type="regular"
				size="extra-small"
				className={styles.forgot}>
				<Link href="/getmail">Забыли пароль?</Link>
			</MainText>
			<MainButton size="large" type="submit" color="blue">
				Войти
			</MainButton>
			<div className={styles['under-btn-text']}>
				<MainText type="regular" size="extra-small">
					Нет аккаунта?
				</MainText>
				<MainText type="regular" size="extra-small" color="blue">
					<Link href="/registration">Зарегистрируйтесь</Link>
				</MainText>
			</div>
		</form>
	);
}

export default LoginPage;
