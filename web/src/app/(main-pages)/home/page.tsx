import { Metadata } from 'next';

import HomePage from '@/page/main/HomePage';

export const metadata: Metadata = {
	title: 'Главная',
};

function Page() {
	return <HomePage />;
}

export default Page;
