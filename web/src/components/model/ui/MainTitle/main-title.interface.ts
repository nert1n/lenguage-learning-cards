import { ReactNode } from 'react';

import { ComponentSize } from '@/config/component-sizes.type';

export interface IMainTitle {
	children: ReactNode;
	size?: ComponentSize;
	className?: string;
}
