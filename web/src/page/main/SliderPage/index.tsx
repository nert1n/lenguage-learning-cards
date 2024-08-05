'use client';

import { useQuery } from 'react-query';
import { useEffect } from 'react';
import dynamic from 'next/dynamic';

import useAuthStore from '@/store/useAuthStore';
import CategoryService from '@/services/category.service';
import TrainService from '@/services/train.service';
import useTrainStore from '@/store/useTrainStore';

const Swiper = dynamic(() => import('@/components/model/widgets/Swiper'));

function SliderPage({
	id,
	isTrain = false,
}: {
	id: number;
	isTrain?: boolean;
}) {
	const { jwt } = useAuthStore(({ jwt }) => ({ jwt }));
	const { setLearning, setLearned } = useTrainStore(
		({ setLearning, setLearned }) => ({ setLearning, setLearned }),
	);
	const getCardsQuery = useQuery(
		['edit', { id, jwt }],
		() => CategoryService.getCategoryCards(jwt, id),
		{ enabled: false },
	);
	const startTrain = useQuery(
		['trainGetCards', { id, jwt }],
		() => TrainService.getTrainStart(jwt, id),
		{ enabled: false },
	);
	useEffect(() => {
		setLearned(0);
		if (isTrain) startTrain.refetch();
		else getCardsQuery.refetch();
	}, []);
	useEffect(() => {
		if (isTrain && startTrain.data !== undefined)
			setLearning(startTrain.data.length);
		else if (getCardsQuery.data !== undefined)
			setLearning(getCardsQuery.data.length);
	}, [startTrain.data, getCardsQuery.data]);
	return (
		<div>
			<Swiper
				cards={
					getCardsQuery.data !== undefined
						? getCardsQuery.data
						: startTrain.data !== undefined
							? startTrain.data
							: []
				}
			/>
		</div>
	);
}

export default SliderPage;
