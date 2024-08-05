import { Metadata } from 'next';

import AllCardsPage from '@/page/main/AllCardsPage';

export const metadata: Metadata = {
	title: 'Мои карточки',
};

function Page() {
	return <AllCardsPage />;
}

export default Page;
