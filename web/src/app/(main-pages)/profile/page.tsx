import { Metadata } from 'next';

import ProfilePage from '@/page/main/ProfilePage';

export const metadata: Metadata = {
	title: 'Профиль',
};

function Page() {
	return <ProfilePage />;
}

export default Page;
