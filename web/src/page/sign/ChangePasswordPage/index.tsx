'use client';

import { toast, ToastContainer } from 'react-toastify';
import { useForm } from 'react-hook-form';
import { useRouter } from 'next/navigation';
import { useMutation } from 'react-query';

import MainTitle from '@/components/model/ui/MainTitle';
import MainText from '@/components/model/ui/MainText';
import MainInput from '@/components/model/ui/MainInput';
import MainButton from '@/components/model/ui/MainButton';
import 'react-toastify/dist/ReactToastify.css';
import useAuthStore from '@/store/useAuthStore';
import AuthService from '@/services/auth.service';
import IChangePasswordPage from '@/page/sign/ChangePasswordPage/change-password-page.interface';

import styles from './ChangePasswordPage.module.scss';

function ChangePasswordPage() {
	const {
		register,
		formState: { errors },
		handleSubmit,
	} = useForm<IChangePasswordPage>({ mode: 'onBlur' });
	const email = useAuthStore(state => state.email);
	const setEmail = useAuthStore(state => state.setEmail);
	const mutation = useMutation((newPassword: string) =>
		AuthService.postAuthChangePassword(newPassword, email),
	);
	const router = useRouter();

	if (Object.values(errors)[0]?.message) {
		toast.error(Object.values(errors)[0].message);
	}

	async function onSubmit({ password, repeat }: IChangePasswordPage) {
		if (password !== repeat) {
			toast.error('Пароли не совпадают');
			return;
		}

		try {
			const { status } = await mutation.mutateAsync(password);
			if (status !== 200) throw Error;
			setEmail('');
			router.push('/login');
		} catch (ex) {
			toast('Ошибка');
		}
	}
	const onError = () => {
		toast.error('Не все поля заполнены!');
	};
	return (
		<form
			className={styles.container}
			onSubmit={handleSubmit(onSubmit, onError)}>
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
			<MainTitle size="large" className={styles.text}>
				Создание нового пароля
			</MainTitle>
			<MainText color="blue" size="small" className={styles['small-text']}>
				Пароль был сброшен, пожалуйста введите новый пароль.
			</MainText>
			<MainInput
				placeholder="Новый пароль"
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
				placeholder="Подтверждение нового пароля"
				type="password"
				className={`${styles.input} ${errors?.repeat && styles.error}`}
				{...register('repeat', {
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
			<MainButton size="large" color="blue" type="submit">
				Подтвердить
			</MainButton>
			<MainText
				size="extra-small"
				type="regular"
				color="blue"
				className={styles.back}>
				<button
					onClick={() => router.back()}
					type="button"
					aria-label="Return back">
					Вернуться назад
				</button>
			</MainText>
		</form>
	);
}

export default ChangePasswordPage;
