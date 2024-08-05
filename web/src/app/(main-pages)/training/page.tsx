import { Metadata } from 'next';

import TrainingPage from '@/page/main/TrainingPage';

export const metadata: Metadata = {
	title: 'Тренировка',
};

function Page() {
	return <TrainingPage />;
}

export default Page;
