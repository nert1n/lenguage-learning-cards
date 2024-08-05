import { Metadata } from 'next';

import CreateCard from '@/page/main/CreateCardPage';

export const metadata: Metadata = {
	title: 'Создание карточки',
};

function CreateCardPage({ params }: { params: { catid: number } }) {
	return <CreateCard catid={params.catid} />;
}

export default CreateCardPage;
