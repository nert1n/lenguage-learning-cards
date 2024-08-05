'use client';

import { useQuery } from 'react-query';
import { useEffect } from 'react';
import { useRouter } from 'next/navigation';

import CardList from '@/components/model/widgets/CardList';
import useAuthStore from '@/store/useAuthStore';
import CategoryService from '@/services/category.service';

function EditCategoryPage({ id }: { id: number }) {
	const router = useRouter();
	const { jwt } = useAuthStore(({ jwt }) => ({ jwt }));
	const { data, isError } = useQuery(
		['edit', { id, jwt }],
		() => CategoryService.getCategoryCards(jwt, id),
		{
			retry: false,
			retryOnMount: false,
		},
	);

	const name = useQuery(['getCatName', { id, jwt }], () =>
		CategoryService.getCategory(jwt, id),
	);

	useEffect(() => {
		if (
			((data !== undefined && data.length === 0) || isError) &&
			name.data === 'Фразы'
		)
			router.push('/home');
	}, [data, isError]);

	return (
		<div>
			{name.data === 'Фразы' ? (
				<CardList type="speaker" cards={data} href="/home" />
			) : (
				<CardList
					type="incategory"
					categoryId={id}
					cards={data}
					href={`/create-card/${id}`}
				/>
			)}
		</div>
	);
}

export default EditCategoryPage;
