import { ButtonHTMLAttributes, ReactNode } from 'react';

import { ComponentSize } from '@/config/component-sizes.type';

export interface IMainButton extends ButtonHTMLAttributes<HTMLButtonElement> {
	children: ReactNode;
	color?: string;
	size?: ComponentSize;
	className?: string;
}
