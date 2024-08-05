import { Metadata } from 'next';

import EditIncategoryPage from '@/page/main/EditIncategoryPage';

export const metadata: Metadata = {
	title: 'Редактирование карточки',
};

function EditIncategory({ params }: { params: { id: number } }) {
	return <EditIncategoryPage id={params.id} />;
}

export default EditIncategory;
