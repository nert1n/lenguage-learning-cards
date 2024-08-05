import { Metadata } from 'next';

import EditCategoryPage from '@/page/main/EditCategoryPage';

export const metadata: Metadata = {
	title: 'Редактирование категории',
};

function EditCategory({ params }: { params: { id: number } }) {
	return <EditCategoryPage id={params.id} />;
}

export default EditCategory;
