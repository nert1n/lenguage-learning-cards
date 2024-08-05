'use client';

import { useForm } from 'react-hook-form';
import { toast, ToastContainer } from 'react-toastify';
import { useMutation } from 'react-query';
import { useRouter } from 'next/navigation';

import useAuthStore from '@/store/useAuthStore';
import MainTitle from '@/components/model/ui/MainTitle';
import MainText from '@/components/model/ui/MainText';
import MainInput from '@/components/model/ui/MainInput';
import MainButton from '@/components/model/ui/MainButton';
import 'react-toastify/dist/ReactToastify.css';
import AuthService from '@/services/auth.service';
import IGetMailPage from '@/page/sign/GetMailPage/get-mail-page.interface';

import styles from './GetEmailPage.module.scss';

function GetMailPage() {
	const {
		register,
		formState: { errors },
		handleSubmit,
	} = useForm<IGetMailPage>({ mode: 'onBlur' });
	const mutation = useMutation((email: string) => AuthService.postEmail(email));
	const setEmail = useAuthStore(state => state.setEmail);
	const router = useRouter();

	if (Object.values(errors)[0]?.message) {
		toast.error(Object.values(errors)[0].message);
	}

	async function onSubmit({ email }: IGetMailPage) {
		try {
			const { status } = await mutation.mutateAsync(email);
			if (status !== 200) throw Error;
		} catch {
			toast('Почта не существует');
			setEmail('');
			return;
		}
		setEmail(email);
		router.push('/resetcode');
	}

	return (
		<div className={styles.wrapper}>
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
				<div className={styles.title}>
					<MainTitle size="large" className={styles.text}>
						Востановлене пароля
					</MainTitle>
					<MainText
						color="blue"
						type="bold"
						size="small"
						className={styles.text}>
						Введите почту для востановления пароля.
					</MainText>
				</div>
				<MainInput
					placeholder="Email"
					type="text"
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
								/^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/i.test(
									value,
								) || 'Неверный формат электронной почты!',
						},
					})}
				/>
				<MainButton size="large" type="submit">
					Отправить код
				</MainButton>
			</form>
			<button onClick={() => router.back()}>
				<MainText color="blue" type="regular" size="extra-small">
					Вернуться назад
				</MainText>
			</button>
		</div>
	);
}

export default GetMailPage;
