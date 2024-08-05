'use client';

import { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';
import { useMutation } from 'react-query';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { redirect } from 'next/navigation';

import useAuthStore from '@/store/useAuthStore';
import MainButton from '@/components/model/ui/MainButton';
import MainInput from '@/components/model/ui/MainInput';
import MainTitle from '@/components/model/ui/MainTitle';
import MainText from '@/components/model/ui/MainText';
import AuthService from '@/services/auth.service';
import IActivateEmailPage from '@/page/sign/ActivateEmailPage/acitivate-email-page.interface';

import styles from './ActivateEmailPage.module.scss';

function ActivateEmailPage() {
	const {
		register,
		formState: { errors },
		handleSubmit,
	} = useForm<IActivateEmailPage>({ mode: 'onBlur' });
	const [time, setTime] = useState<number>(180);
	const [sendCode, setSendCode] = useState<boolean>(false);
	const [firstSend, setFirstSend] = useState<boolean>(true);
	const setJwt = useAuthStore(state => state.setJwt);
	const mutationGet = useMutation(() => AuthService.getAuthToken());
	const mutationPost = useMutation((code: string) =>
		AuthService.postAuthActivate(code),
	);

	useEffect(() => {
		if (firstSend) {
			mutationGet.mutate();
			setFirstSend(false);
		}
		if (time === 0) {
			setSendCode(true);
		}
		setTimeout(() => {
			if (time > 0) setTime(time - 1);
		}, 1000);
	}, [time]);

	function formatTime(duration: number) {
		const minutes = duration / 60;
		const seconds = duration % 60;
		if (seconds < 10) return `${parseInt(minutes.toString(), 10)}:0${seconds}`;
		return `${parseInt(minutes.toString(), 10)}:${seconds}`;
	}

	function onSubmit(answer: IActivateEmailPage) {
		try {
			mutationPost.mutate(answer.code);
			if (mutationPost.isError) {
				throw new Error('Некоректный код');
			}
			if (mutationPost.data !== undefined) setJwt(mutationPost.data.data);
			redirect('/main');
		} catch (error: any) {
			toast(error.message);
		}
	}

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
			<MainTitle size="large" className={styles.text}>
				Подтверждение почты
			</MainTitle>
			<MainText color="blue" type="bold" size="small" className={styles.text}>
				Вам на почту будет отправлен код с подтверждением вашей почты.
				Пожалуйста введите его в данное поле.
			</MainText>
			<MainInput
				placeholder="Код"
				type="text"
				className={`${styles.input} ${errors?.code && styles.error}`}
				{...register('code', {
					required: 'Пожалуйста введите код!',
					minLength: {
						value: 3,
						message: 'Код слишком короткий!',
					},
					maxLength: {
						value: 6,
						message: 'Код слишком длинный!',
					},
					validate: {
						onlyDigits: value =>
							/^[0-9]+$/.test(value) || 'Поле должно содержать только цифры!',
					},
				})}
			/>
			<MainButton size="large" type="submit">
				Подтвердить
			</MainButton>
			<div className={styles['under-btn-text']}>
				<MainText
					type="bold"
					size="extra-small"
					className={styles.send}
					color="blue">
					<button
						onClick={() => {
							if (sendCode) {
								setSendCode(false);
								mutationGet.mutate();
								setTime(180);
							}
						}}
						type="button"
						aria-label="Send code">
						Отправить код повторно через:
					</button>
				</MainText>
				<MainText type="bold" size="extra-small">
					{formatTime(time)}
				</MainText>
			</div>
		</form>
	);
}

export default ActivateEmailPage;
