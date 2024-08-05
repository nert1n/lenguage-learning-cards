import { Metadata } from 'next';

import PhrasesPage from '@/page/main/PhrasesPage';

export const metadata: Metadata = {
	title: 'Разговорные фразы',
};

function Page() {
	return <PhrasesPage />;
}

export default Page;
