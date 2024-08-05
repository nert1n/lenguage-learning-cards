import { Metadata } from 'next';

import CategoriesPage from '@/page/main/CategoriesPage';

export const metadata: Metadata = {
	title: 'Категории',
};

function Page() {
	return <CategoriesPage />;
}

export default Page;
