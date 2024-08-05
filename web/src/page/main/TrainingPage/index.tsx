'use client';

import { useQuery } from 'react-query';

import CardList from '@/components/model/widgets/CardList';
import useAuthStore from '@/store/useAuthStore';
import TrainService from '@/services/train.service';

function TrainingPage() {
	const { jwt } = useAuthStore(({ jwt }) => ({ jwt }));
	const trains = useQuery(['getTrains', { jwt }], () =>
		TrainService.getTrain(jwt),
	);
	return <CardList trains={trains.data} type="train" href="" />;
}

export default TrainingPage;
