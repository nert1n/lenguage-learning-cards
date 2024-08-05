import { Metadata } from 'next';

import CreateCategoryPage from '@/page/main/CreateCategoryPage';

export const metadata: Metadata = {
	title: 'Создание категории',
};

function Page() {
	return <CreateCategoryPage />;
}

export default Page;
