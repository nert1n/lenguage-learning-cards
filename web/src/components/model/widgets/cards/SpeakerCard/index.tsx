'use client';

import { SquarePlus, Volume2 } from 'lucide-react';
import cn from 'clsx';
import { usePathname } from 'next/navigation';

import MainText from '@/components/model/ui/MainText';
import MainTitle from '@/components/model/ui/MainTitle';
import useSpeakerCard from '@/hooks/useSpeakerCard';

import styles from './SpeakerCard.module.scss';
import { ISpeakerCard } from './speaker-card.interface';

function SpeakerCard({
	children = '',
	translation,
	id,
	isInFavorite,
}: ISpeakerCard) {
	const path = usePathname();
	const { active, volumeActive, handleClickVolume, handleClickPlus } =
		useSpeakerCard({ id, children, isInFavorite });

	return (
		<div className={styles.card}>
			<Volume2
				stroke="var(--color-icons)"
				size={23}
				strokeWidth={3}
				className={cn(
					styles['volume-icon'],
					volumeActive && styles['volume-icon_blue'],
				)}
				onClick={() => handleClickVolume()}
			/>
			{path !== '/my-cards' && (
				<SquarePlus
					stroke={
						active || path === '/dictionary' ? '#5EB84F' : 'var(--color-icons)'
					}
					size={23}
					strokeWidth={3}
					className={styles['square-plus-icon']}
					onClick={handleClickPlus}
				/>
			)}
			<div className={styles.content}>
				<MainTitle size="extra-small" className={styles.word}>
					{children}
				</MainTitle>
				<MainText size="small" color="gray">
					{translation}
				</MainText>
			</div>
		</div>
	);
}

export default SpeakerCard;
