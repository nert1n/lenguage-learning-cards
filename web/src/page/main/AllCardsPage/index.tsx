'use client';

import dynamic from 'next/dynamic';
import { useEffect } from 'react';
import { useQuery } from 'react-query';

import CardsService from '@/services/cards.service';
import useAuthStore from '@/store/useAuthStore';
import useTitleStore from '@/store/useTitleStore';

const CardList = dynamic(() => import('@/components/model/widgets/CardList'));

export interface ICardInterface {
	id: number;
	engtext: string;
	rustext: string;
	inFavorites?: boolean;
}

function AllCardsPage() {
	const { jwt } = useAuthStore(({ jwt }) => ({ jwt }));
	const { setTitle } = useTitleStore(({ setTitle }) => ({ setTitle }));
	const query = useQuery(['getAuthorCards', { jwt }], () =>
		CardsService.getOwnCards(jwt),
	);
	useEffect(() => setTitle('Мои карточки'), []);
	return (
		<CardList type="speaker" cards={query.data} href="/create-card" plus />
	);
}

export default AllCardsPage;
