import { ReactNode } from 'react';

import { ComponentSize } from '@/config/component-sizes.type';
import { FontWeight } from '@/config/font-weight.type';
import { TextColor } from '@/config/text-color.type';

export interface IMainText {
	children: ReactNode;
	size?: ComponentSize;
	type?: FontWeight;
	className?: string;
	color?: TextColor;
}
