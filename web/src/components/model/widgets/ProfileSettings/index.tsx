'use client';

import { usePathname } from 'next/navigation';
import cn from 'clsx';

import MainTitle from '../../ui/MainTitle';
import styles from './Settings.module.scss';

function ProfileSettings() {
	const path = usePathname();
	return (
		<div
			className={cn(styles.settings, path === '/settings' && styles.animate)}>
			<MainTitle size="large" className={styles.title}>
				Скоро...
			</MainTitle>
		</div>
	);
}

export default ProfileSettings;
